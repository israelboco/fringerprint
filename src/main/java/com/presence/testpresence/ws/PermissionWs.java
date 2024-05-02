package com.presence.testpresence.ws;
import com.presence.testpresence.model.enums.TypePermissionEnum;
import lombok.Data;

@Data
public class PermissionWs {
    private Integer id;
    private TypePermissionEnum type;
    private String description;
    private EmployeeWs employeeWs;
    private long startDateTimestamp;
    private long endDateTimestamp;
    private Boolean accepted;
}
