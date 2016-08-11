#Scala Tx Provider

CxProvider is a trait that grabs a sql connection from a datasource and wraps it in an arm managed resource

TxProvider adds a rollback on exception

RollbackProvider rolls back any sql changes. Very useful for tests.