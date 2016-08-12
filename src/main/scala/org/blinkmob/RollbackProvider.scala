package org.blinkmob

import java.sql.Connection

import scala.util.{Failure, Success, Try}

trait RollbackProvider extends CxProvider{

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
