package writerexample

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object Problem extends App {

  def step(id: String)(i: Int): Future[Int] = Future {
    println(s"executing step $i of $id")
    Thread.sleep(300)
    i
  }

  def complexJob: Future[List[Int]] = {
    println("starting complex job!")
    val steps = (0 to 30).toList map { i => step("complex job")(i) }
    Future.sequence(steps).andThen {
      case Success(l) => println(s"complex job finished with $l")
    }
  }

  def simpleJob: Future[List[Int]] = {
    println("starting simple job!")
    val steps = (0 to 15).toList map { i => step("simple job")(i) }
    Future.sequence(steps).andThen {
      case Success(l) => println(s"simple job finished with $l")
    }
  }

  val jobs = List(complexJob, simpleJob)

  Await.result(Future.sequence(jobs), Duration.Inf)
}
