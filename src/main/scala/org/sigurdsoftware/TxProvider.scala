import java.sql.Connection
import javax.sql.DataSource

import resource._

import scala.util.control.{ControlThrowable, NonFatal}
import scala.util.{Failure, Success, Try}


trait CxProvider{
  val ds:DataSource

  def cx[A](block: Connection => A): A = managed(ds.getConnection()).acquireAndGet(block(_))
}

trait TxProvider extends CxProvider{

  def tx[A](block: Connection => A): A ={
    cx { c =>
      c.setAutoCommit(false)
      Try({
        val r = block(c)
        c.commit()
        r
      }) match{
        case Success(r) => r
        case Failure(f) => {
          f match{
            case e: ControlThrowable => c.commit(); throw e
            case e => c.rollback(); throw e
          }
        }
      }


    }
  }

}