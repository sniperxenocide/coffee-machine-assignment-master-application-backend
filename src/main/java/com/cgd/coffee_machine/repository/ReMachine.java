package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.Machine;
import com.cgd.coffee_machine.report.MachineSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReMachine extends JpaRepository<Machine, Long>, JpaSpecificationExecutor<Machine> {

    @Query(value = "select * from machine where id like ?1 and machine_number like ?2 ",
    nativeQuery = true)
    Page<Machine> getMachinesWithFilterAndPagination(String id,String machineNumber,Pageable pageable);

    @Query(value = " select machine.* from machine inner join  " +
            " (select * from " +
            "    (select brand_id, " +
            "           id, " +
            "           row_number() over (partition by brand_id order by rand() ) as brand_rank " +
            "    from machine where machine.id not in (select c.machine_id from contract c) ) ranks " +
            " where brand_rank <= ?1 " +
            " ) as A on machine.id = A.id ",
    nativeQuery = true)
    List<Machine> getMachineForEachBrandWithNoContract(int count);



    @Query(
            value = "select new com.cgd.coffee_machine.report.MachineSummary( b.name,oc.name, co.name,count(m)," +
            "sum(case when c.id is null then 0 else 1 end)," +
             "sum(case when c.id is null then 1 else 0 end)) " +
            "from Machine m " +
            "left join MachineBrand b on m.machineBrand.id = b.id " +
            "left join OriginCountry oc on m.originCountry.id = oc.id " +
            "left join ChamberOption co on m.chamberOption.id = co.id " +
            "left join Contract c on m.id = c.machine.id " +
            "group by b.name,oc.name,co.name"
    )
    List<MachineSummary> findBrandCountryWiseMachineSummary();


}