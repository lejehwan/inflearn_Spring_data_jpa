package study.datajpa.repository;

import org.apache.el.util.Validation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // query from method name
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // query from @Query annotation
    @Query("select m from Member m where m.username=:username and m.age=:age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);// collection

    Member findMemberByUsername(String username);// one

    Optional<Member> findOptionalByUsername(String username);// optional

    // paging query
//    @Query(value = "select m from Member m left join m.team t")
    @Query(value = "select m from Member m", countQuery = "select count(m) from Member m")// distribute count query
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findByAge(Pageable pageable, int age);

    // bulk
    @Modifying(clearAutomatically = true)// executeUpdate() & entityManger auto clear
    @Query(value = "update Member m set m.age=m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}
