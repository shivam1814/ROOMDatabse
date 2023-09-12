package com.example.roomdatabse

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentDao {

    @Query("SELECT * FROM student_table")
    fun getAll():List<Student>

    @Query("SELECT EXISTS(SELECT roll_no FROM student_table WHERE roll_no = :rollNumber)")
    suspend fun findRollNoData(rollNumber:Int):Boolean

    @Query("SELECT * FROM student_table WHERE roll_no LIKE :roll LIMIT 1")
    suspend fun getRollNoData(roll:Int):Student

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Query("DELETE FROM student_table WHERE roll_no LIKE :roll")
    suspend fun delete(roll: Int)

    @Query("DELETE FROM student_table")
    suspend fun deletAll()

}