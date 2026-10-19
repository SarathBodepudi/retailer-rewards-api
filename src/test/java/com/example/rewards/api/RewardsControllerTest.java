package com.example.rewards.api;
import com.example.rewards.controller.RewardsController;
import com.example.rewards.exception.GlobalExceptionHandler;
import com.example.rewards.service.RewardsService;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
class RewardsControllerTest {
 private RewardsService service;private MockMvc mvc;
 @BeforeEach void setup(){service=mock(RewardsService.class);mvc=MockMvcBuilders.standaloneSetup(new RewardsController(service)).setControllerAdvice(new GlobalExceptionHandler()).build();}
 @Test void returns200AndMonthlyRewards() throws Exception {
  when(service.report(YearMonth.of(2026,1))).thenReturn(new RewardsReport(YearMonth.of(2026,1),YearMonth.of(2026,3),Collections.singletonList(new CustomerRewards("C001",Collections.singletonList(new MonthlyRewards(YearMonth.of(2026,1),90)),90))));
  mvc.perform(get("/api/v1/rewards?startMonth=2026-01")).andExpect(status().isOk()).andExpect(jsonPath("$.customers[0].totalPoints").value(90));
  verify(service).report(YearMonth.of(2026,1));
 }
 @Test void invalidMonthHasConsistent400Error() throws Exception {
  mvc.perform(get("/api/v1/rewards?startMonth=January")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.path").value("/api/v1/rewards"));
  verifyNoInteractions(service);
 }
 @Test void unknownCustomerReturns404() throws Exception {
  when(service.customerReport(YearMonth.of(2026,1),"C999")).thenReturn(Optional.empty());
  mvc.perform(get("/api/v1/rewards/C999?startMonth=2026-01")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
 }
}
