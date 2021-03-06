# ScalikeJDBC Config

This is an easy-to-use configuration loader for ScalikeJDBC which reads typesafe config.

https://github.com/typesafehub/config

## How to use

### project/Build.scala

```scala
libraryDependencies ++= Seq(
  "com.github.seratch" %% "scalikejdbc"        % "[1.5,)",
  "com.github.seratch" %% "scalikejdbc-config" % "[1.5,)"
)
```

### Working with default database

Connection pool for the database named 'default' can be set up with DBs#setup and closed with DBs#close

```
db.default.url="jdbc:h2:mem:sample1"
db.default.driver="org.h2.Driver"
db.default.user="sa"
db.default.password="secret"

db.default.poolInitialSize=10
db.default.poolMaxSize=10
db.default.poolValidationQuery="select 1 as one"
```

```scala
scala> import scalikejdbc._

scala> import scalikejdbc.config._

scala> DBs.setup()

scala> DB readOnly { implicit session =>
     |   SQL("SELECT 1 as one").map(rs => rs.int("one")).single.apply()
     | }
res1: Option[Int] = Some(1)

scala> DBs.close()
```


### Working with named databases

src/main/resources/application.conf
```
db.foo.url="jdbc:h2:mem:sample2"
db.foo.driver="org.h2.Driver"
db.foo.user="sa"
db.foo.password="secret"

db.bar.url="jdbc:h2:mem:sample2"
db.bar.driver="org.h2.Driver"
db.bar.user="sa2"
db.bar.password="secret2"
```


```scala
scala> import scalikejdbc._

scala> import scalikejdbc.config._

scala> DBs.setupAll

scala> NamedDB('foo) readOnly { implicit session =>
     |   SQL("SELECT 1 as one").map(rs => rs.int("one")).single.apply()
     | }
res0: Option[Int] = Some(1)

scala> NamedDB('bar) readOnly { implicit session =>
     |   SQL("SELECT 1 as one").map(rs => rs.int("one")).single.apply()
     | }
res1: Option[Int] = Some(1)

scala> DBs.closeAll
```
