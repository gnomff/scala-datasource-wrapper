#Scala Tx Provider

CxProvider is a trait that grabs a sql connection from a datasource and wraps it in an arm managed resource

Available methods are cx, tx and rbtx. rbtx rolls back the transaction regardless of success or failure.

Example usage:

```
object DB extends CxProvider with hasDataSource{
    val ds = //yourdatasourcehere
}

DB.tx{implicit c=>
    SQL"""select 1 from stanalone_anorm_is_fun""".as(scalar[Int].single)
}
```