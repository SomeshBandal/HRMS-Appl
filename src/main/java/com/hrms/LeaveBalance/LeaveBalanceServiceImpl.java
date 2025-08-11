package com.hrms.LeaveBalance;

import com.hrms.Employee.EmployeeNotFoundException;
import com.hrms.Entity.Employee;
import com.hrms.Entity.Enum.LeaveType;
import com.hrms.Entity.LeaveBalance;
import com.hrms.Employee.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LeaveBalanceServiceImpl implements LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveBalanceMapper leaveBalanceMapper;

    @Override
    public LeaveBalanceDto createLeaveBalance(Long employeeId, LeaveBalanceDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + employeeId));

        LeaveBalance balance = leaveBalanceMapper.toEntity(dto, employee);
        LeaveBalance saved = leaveBalanceRepository.save(balance);
        return leaveBalanceMapper.toDto(saved);
    }

    @Override
    public LeaveBalanceDto updateLeaveBalance(Long id, LeaveBalanceDto dto) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findById(id)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found with ID: " + id));

        leaveBalance.setTotalLeaves(dto.getTotalLeaves());
        leaveBalance.setUsedLeaves(dto.getUsedLeaves());
        leaveBalance.setRemainingLeaves(dto.getRemainingLeaves());

        LeaveBalance updated = leaveBalanceRepository.save(leaveBalance);
        return leaveBalanceMapper.toDto(updated);
    }

    @Override
    public List<LeaveBalanceDto> getLeaveBalanceById(Long employeeId) {
        List<LeaveBalance> balance = leaveBalanceRepository.findByEmployeeId(employeeId);

        if (balance.isEmpty()){
            throw new LeaveBalanceNotFoundException("Leave balance not found for employee");
        }
        return balance.stream().map(leaveBalanceMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public void deleteLeaveBalance(Long id) {
        leaveBalanceRepository.deleteById(id);
    }

    @Override
    public boolean hasSufficientLeave(Long employeeId, String leaveTypeStr, int requestedDays) {
        LeaveType leaveType = LeaveType.valueOf(leaveTypeStr.toUpperCase());

        LeaveBalance leaveBalance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(employeeId, leaveType)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found for type: " + leaveType));

        return leaveBalance.getRemainingLeaves() >= requestedDays;
    }

    @Override
    public void deductLeave(Long employeeId, String leaveTypeStr, int daysUsed) {
        LeaveType leaveType = LeaveType.valueOf(leaveTypeStr.toUpperCase());

        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveType(employeeId, leaveType)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found for type: " + leaveType));

        if (balance.getRemainingLeaves() < daysUsed) {
            throw new IllegalStateException("Insufficient leave balance");
        }

        balance.setUsedLeaves(balance.getUsedLeaves() + daysUsed);
        balance.setRemainingLeaves(balance.getRemainingLeaves() - daysUsed);

        leaveBalanceRepository.save(balance);
    }

}
