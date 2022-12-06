package com.cgd.coffee_machine.repository;

import com.cgd.coffee_machine.model.Shop;
import com.cgd.coffee_machine.report.LocationWiseShopSummary;
import com.cgd.coffee_machine.report.TypeWiseShopSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReShop extends JpaRepository<Shop, Long>, JpaSpecificationExecutor<Shop> {
    Optional<Shop> findByShopCode(String shopCode);


    @Query(value = " select new com.cgd.coffee_machine.report.LocationWiseShopSummary( " +
            " s.division,s.region,s.territory, " +
            " count(distinct s.id) ,count(c.id)," +
            " sum(case when c.id is null then 1 else 0 end) ) from Shop s " +
            " left join Contract c on s.id = c.shop.id " +
            " where c.creationTime between ?1 and ?2 "+
            " group by s.division,s.region,s.territory "
    )
    List<LocationWiseShopSummary> getTerritoryWiseShopSummary(LocalDateTime start,LocalDateTime end);

    @Query(value = " select new com.cgd.coffee_machine.report.LocationWiseShopSummary( " +
            " s.division,s.region,'', " +
            " count(distinct s.id) ,count(c.id)," +
            " sum(case when c.id is null then 1 else 0 end) ) from Shop s " +
            " left join Contract c on s.id = c.shop.id " +
            " where c.creationTime between ?1 and ?2 "+
            " group by s.division,s.region "
    )
    List<LocationWiseShopSummary> getRegionWiseShopSummary(LocalDateTime start,LocalDateTime end);

    @Query(value = " select new com.cgd.coffee_machine.report.LocationWiseShopSummary( " +
            " s.division,'','', " +
            " count(distinct s.id) ,count(c.id)," +
            " sum(case when c.id is null then 1 else 0 end) ) from Shop s " +
            " left join Contract c on s.id = c.shop.id " +
            " where c.creationTime between ?1 and ?2 "+
            " group by s.division "
    )
    List<LocationWiseShopSummary> getDivisionWiseShopSummary(LocalDateTime start,LocalDateTime end);


    @Query(value = "select new com.cgd.coffee_machine.report.TypeWiseShopSummary" +
            "(t.name ,count(distinct s.id) ,count(c.id)," +
            "sum(case when c.id is null and s.id is not null then 1 else 0 end))" +
            "from ShopType t " +
            "left join Shop s on s.shopType.id = t.id " +
            "left join Contract c on s.id = c.shop.id " +
            "group by t.name "
    )
    List<TypeWiseShopSummary> getTypeWiseShopSummary();

    @Query(value = " select * from shop where territory like ?1 " +
            "order by division desc,region,territory,distributor_oracle_code,shop_type_id ",
    nativeQuery = true)
    List<Shop> getAllShopByTerritory(String territory);

}