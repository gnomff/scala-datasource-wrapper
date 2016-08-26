package org.blinkmob

import java.sql.Connection
import javax.sql.DataSource

import resource._

import scala.util.control.ControlThrowable
import scala.util.{Failure, Success, Try}


/**
  * CxProvider provides three methods - cx, tx and rbtx. cx provides an autocommit java.Sql.Connection. tx
  * provides a java.sql.Connection that is committed on success and rolled back on an
  * Exception. rbtx rolls the transaction back even after a success - this is very useful for testing.
  *
  */
trait CxProvider{ this:hasDataSource =>

  def cx[A](block: Connection => A): A = managed(ds.getConnection()).acquireAndGet(block(_))

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

  def rbtx[A](block: Connection => A): A ={
    cx { c =>
      c.setAutoCommit(false)
      Try(block(c)) match{
        case Success(r) => c.rollback(); r
        case Failure(f) => c.rollback(); throw f
      }
    }
  }

}
