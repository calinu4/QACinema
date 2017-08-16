package CoverageTests

import models._
import org.scalatest.FlatSpec
class ModelsTest extends FlatSpec {
  val m = Movie(1, "1", "1", "1", "1", "1", "12A", 5, "2017-08-05", 120, "Action", "1", "1", "1", "1")

  "Movie Name" should "be 1" in {
    assert(m.title === "1")
  }
  "Movie ID" should "be 1" in {
    assert(m.movie_id === 1)
  }
  "Movie Poster link" should "be 1" in {
    assert(m.poster_path === "1")
  }
  "Movie Landscape Link" should "be 1" in {
    assert(m.landscape === "1")
  }
  "Movie Video Link" should "be 1" in {
    assert(m.video === "1")
  }
  "Movie Images Link" should "be 1" in {
    assert(m.images === "1")
  }
  "Movie User Rating" should "be 5" in {
    assert(m.user_rating === 5)
  }
  "Movie Age Rating" should "be 12A" in {
    assert(m.age_rating === "12A")
  }

  "Movie Release Date" should "be 2017-08-05" in {
    assert(m.release_date === "2017-08-05")
  }
  "Movie Run Time" should "be 120" in {
    assert(m.run_time === 120)
  }
  "Movie Genre" should "be Action" in {
    assert(m.genres === "Action")
  }
  "Movie Description" should "be 1" in {
    assert(m.overview === "1")
  }
  "Movie Cast" should "be 1" in {
    assert(m.cast === "1")
  }
  "Movie Director" should "be 1" in {
    assert(m.director === "1")
  }
  "Movie Screen" should "be 1" in {
    assert(m.screen === "1")
  }

  val c=ContactUs("name","email","subject","message")

  "Contact Name" should "be name" in {
    assert(c.name === "name")
  }
  "Contact Email" should "be email" in {
    assert(c.email === "email")
  }
  "Contact Subject" should "be subject" in {
    assert(c.subject === "subject")
  }
  "Contact Message" should "be message" in {
    assert(c.message === "message")
  }

  val r=Reservation("1","1","1",1,1,1,List(List("2","1")),1,"1","1","1","1",true)

  "Reservation Id" should "be 1" in {
    assert(r.reservationId === "1")
  }
  "Reservation Paid" should "be true" in {
    assert(r.paid === true)
  }
  "Reservation Seats" should "be List(List(2,1))" in {
    assert(r.seats === List(List("2","1")))
  }
  "Reservation Adult" should "be 1" in {
    assert(r.adult === 1)
  }
  "Reservation Child" should "be 1" in {
    assert(r.child === 1)
  }
  "Reservation Concession" should "be 1" in {
    assert(r.concession === 1)
  }
  "Reservation Total" should "be 1" in {
    assert(r.total === 1)
  }
  "Reservation Room" should "be 1" in {
    assert(r.room === "1")
  }
  "Reservation Time" should "be 1" in {
    assert(r.time === "1")
  }
  "Reservation Date" should "be 1" in {
    assert(r.date === "1")
  }
  "Reservation Movie" should "be 1" in {
    assert(r.movie === "1")
  }
  "Reservation Email" should "be 1" in {
    assert(r.email === "1")
  }
  "Reservation Name" should "be 1" in {
    assert(r.name === "1")
  }

  val s=Showing(1,1,"1","1","1",1,Array(Array(1,2)))

  "Showing Id" should "be 1" in {
    assert(s.showingId === 1)
  }
  "Showing RoomId" should "be 1" in {
    assert(s.roomId === 1)
  }
  "Showing Movie Name" should "be 1" in {
    assert(s.movieId === "1")
  }
  "Showing Seats No" should "be 1" in {
    assert(s.seatsAvailable === 1)
  }
  "Showing Time" should "be 1" in {
    assert(s.time === "1")
  }
  "Showing Date" should "be 1" in {
    assert(s.date === "1")
  }
  "Showing Seats" should "be (1,2)" in {
    assert(s.seats ===Array(Array(1,2)))
  }
}

