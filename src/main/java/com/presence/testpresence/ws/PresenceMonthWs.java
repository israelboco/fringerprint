package com.presence.testpresence.ws;

import lombok.Data;

import java.util.List;

@Data
public class PresenceMonthWs {
    private EmployeeWs employeeWs;
    private List<JourWs> jours;
}
