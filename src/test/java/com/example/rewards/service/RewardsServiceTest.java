package com.example.rewards.service;
import com.example.rewards.api.CustomerRewards;
import com.example.rewards.api.RewardsReport;
import com.example.rewards.domain.Transaction;
import com.example.rewards.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RewardsServiceTest {
 @Mock TransactionRepository repository;
 private RewardsService service;
 @BeforeEach void setup(){service=new RewardsService(repository,new RewardCalculator());}
 @Test void groupsTransactionsByMonthAndCustomerAndExcludesOtherMonths(){
  when(repository.findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan( LocalDate.of(2026,1,1), LocalDate.of(2026,4,1))).thenReturn(Arrays.asList(
   tx("1","C001","2026-01-01","120"),tx("2","C001","2026-01-31","75"),
   tx("3","C001","2026-03-31","200"),tx("4","C002","2026-02-01","100"),
   tx("5","C001","2025-12-31","1000"),tx("6","C001","2026-04-01","1000")));
  RewardsReport result=service.report(YearMonth.of(2026,1));
  assertEquals(2,result.getCustomers().size());
  CustomerRewards first=result.getCustomers().get(0);
  assertEquals("C001",first.getCustomerId());
  assertEquals(Arrays.asList(115L,0L,250L),first.getMonthlyRewards().stream().map(m->m.getPoints()).collect(Collectors.toList()));
  assertEquals(365,first.getTotalPoints());
  assertEquals(50,result.getCustomers().get(1).getTotalPoints());
  verify(repository,times(1)).findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(LocalDate.of(2026,1,1),LocalDate.of(2026,4,1));
 }
 @Test void returnsEmptyOptionalForUnknownCustomer(){
  when(repository.findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan( LocalDate.of(2026,1,1), LocalDate.of(2026,4,1))).thenReturn(Collections.emptyList());
  Optional<CustomerRewards> customer=service.customerReport(YearMonth.of(2026,1),"UNKNOWN");
  assertFalse(customer.isPresent());verify(repository).findByPurchaseDateGreaterThanEqualAndPurchaseDateLessThan(LocalDate.of(2026,1,1),LocalDate.of(2026,4,1));
 }
 private Transaction tx(String id,String customer,String date,String amount){return new Transaction(id,customer,LocalDate.parse(date),new BigDecimal(amount));}
}
