package com.example.anorm

import scalikejdbc.specs2.AutoRollback
import org.specs2._
import org.joda.time._
import scalikejdbc._

class MemberSpec extends Specification with com.example.DBSettings {
  def is =

    sequential ^
      "The 'Member' model should" ^
      "find by primary keys" ! autoRollback().findByPrimaryKeys ^
      "find all records" ! autoRollback().findAll ^
      "count all records" ! autoRollback().countAll ^
      "find by where clauses" ! autoRollback().findAllBy ^
      "count by where clauses" ! autoRollback().countBy ^
      "create new record" ! autoRollback().create ^
      "update a record" ! autoRollback().update ^
      "delete a record" ! autoRollback().delete ^
      end

  case class autoRollback() extends AutoRollback {

    override def fixture(implicit session: DBSession) {
      SQL("insert into member (id, name, created_at) values (?, ?, ?)")
        .bind(123, "123", DateTime.now).update.apply()
    }

    def findByPrimaryKeys = this{
      val maybeFound = Member.find(123)
      maybeFound.isDefined should beTrue
    }
    def findAll = this{
      val allResults = Member.findAll()
      allResults.size should be_>(0)
    }
    def countAll = this{
      val count = Member.countAll()
      count should be_>(0L)
    }
    def findAllBy = this{
      val results = Member.findAllBy("ID = {id}", 'id -> 123)
      results.size should be_>(0)
    }
    def countBy = this{
      val count = Member.countBy("ID = {id}", 'id -> 123)
      count should be_>(0L)
    }
    def create = this{
      val created = Member.create(name = "MyString", createdAt = DateTime.now)
      created should not beNull
    }
    def update = this{
      val entity = Member.findAll().head
      val updated = Member.update(entity.copy(name = "Updated"))
      updated should not be_== (entity)
    }
    def delete = this{
      Member.find(123).map { entity =>
        Member.delete(entity)
      }
      val shouldBeNone = Member.find(123)
      shouldBeNone.isDefined should beFalse
    }
  }

}
