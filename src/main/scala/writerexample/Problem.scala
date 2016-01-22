package writerexample

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Success

/**
  * naive logging in future often generates interleaving entries
  * this code demostrate the issue
  */
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
      case Success(results) => println(s"complex job finished with $results")
    }
  }

  def simpleJob: Future[List[Int]] = {
    println("starting simple job!")
    val steps = (0 to 15).toList map { i => step("simple job")(i) }
    Future.sequence(steps).andThen {
      case Success(results) => println(s"simple job finished with $results")
    }
  }

  val jobs = List(complexJob, simpleJob)

  Await.result(Future.sequence(jobs), Duration.Inf)
}
