package Room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Room.entities.Department;


@Dao
public interface DepartmentDAO {

    @Insert
    void insertDepartment(Department department);

    @Query("SELECT * FROM Departments")
    List<Department> getDepartmentList();

}
