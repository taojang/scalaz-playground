package writerexample

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scalaz.{MonadTell, WriterT}
import scalaz.std.list._
import scalaz.std.scalaFuture._
import scalaz.syntax.traverse._

/**
  * solution using scalaz is to simply use `WriterT`
  * the computation still happens in parallel,
  * but the result is a `Tuple2`, the first element
  * is aggregated log, second element is the computed result
  *
  * `WriterT` is a monad when the first type param is a monad
  * so for comprehension could be used to write
  * easy understandable and nearly imperative code
  */
object Solution extends App {

  type AsyncLogged[A] = WriterT[Future, List[String], A]
  val logger = MonadTell[AsyncLogged, List[String]]

  def step(id: String)(i: Int): AsyncLogged[Int] =
    WriterT.put(Future{ Thread.sleep(300); i})(List(s"executing step $i of $id"))

  def complexJob: AsyncLogged[List[Int]] = for {
    _       <- logger.tell(List("starting complex job"))
    results <- (0 to 30).toList.map({ i => step("complex job")(i) }).sequence
    _       <- logger.tell(List(s"complex job finished with $results"))
  } yield results

  def simpleJob: AsyncLogged[List[Int]] = for {
    _       <- logger.tell(List("starting simple job"))
    results <- (0 to 10).toList.map({ i => step("simple job")(i) }).sequence
    _       <- logger.tell(List(s"simple job finished with $results"))
  } yield results

  val jobs = List(complexJob, simpleJob).map(_.run)

  val res = Await.result(Future.sequence(jobs), Duration.Inf)
  res.map(_._1).flatten.foreach(println)
}
