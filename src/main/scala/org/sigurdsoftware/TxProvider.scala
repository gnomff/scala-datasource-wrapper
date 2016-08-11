package org.sigurdsoftware

import java.sql.Connection
import scala.util.control.{ControlThrowable}
import scala.util.{Failure, Success, Try}

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