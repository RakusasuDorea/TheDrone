package com.drone_project.drone.controller;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.exception.DroneNotFoundException;
import com.drone_project.drone.model.Model;
import com.drone_project.drone.model.State;
import com.drone_project.drone.service.DroneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DroneController.class)
class DroneControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DroneService droneService;

    // ── GET /{serialNumber} ──────────────────────────────────────────────────

    @Test
    void getDroneInfo_returns200WhenFound() throws Exception {
        DroneResponseDTO response = droneResponse("DRONE001");
        when(droneService.getDroneInfo("DRONE001")).thenReturn(response);

        mockMvc.perform(get("/api/v1/drones/DRONE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"))
                .andExpect(jsonPath("$.state").value("IDLE"));
    }

    @Test
    void getDroneInfo_returns404WhenNotFound() throws Exception {
        when(droneService.getDroneInfo("UNKNOWN"))
                .thenThrow(new DroneNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/api/v1/drones/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    // ── POST /register ───────────────────────────────────────────────────────

    @Test
    void registerDrone_returns200WithValidRequest() throws Exception {
        DroneRequestDTO request = new DroneRequestDTO();
        request.setSerialNumber("DRONE001");
        request.setModel(Model.Heavyweight);
        request.setBatteryCapacity(100);

        DroneResponseDTO response = droneResponse("DRONE001");
        when(droneService.registerDrone(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/drones/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"));
    }

    @Test
    void registerDrone_returns400WhenSerialNumberBlank() throws Exception {
        DroneRequestDTO request = new DroneRequestDTO();
        request.setSerialNumber("");
        request.setModel(Model.Heavyweight);
        request.setBatteryCapacity(100);

        mockMvc.perform(post("/api/v1/drones/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /available ───────────────────────────────────────────────────────

    @Test
    void getAvailableDrones_returnsListOfIdleDrones() throws Exception {
        when(droneService.getAvailableDrones()).thenReturn(List.of(droneResponse("DRONE001")));

        mockMvc.perform(get("/api/v1/drones/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].serialNumber").value("DRONE001"));
    }

    // ── GET /{serialNumber}/battery ──────────────────────────────────────────

    @Test
    void getBatteryLevel_returns200WithBatteryInfo() throws Exception {
        when(droneService.getBatteryLevel("DRONE001")).thenReturn(85);

        mockMvc.perform(get("/api/v1/drones/DRONE001/battery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.battery").value(85))
                .andExpect(jsonPath("$.serialNumber").value("DRONE001"));
    }

    @Test
    void getBatteryLevel_returns404WhenDroneNotFound() throws Exception {
        when(droneService.getBatteryLevel("UNKNOWN"))
                .thenThrow(new DroneNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/api/v1/drones/UNKNOWN/battery"))
                .andExpect(status().isNotFound());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private DroneResponseDTO droneResponse(String serialNumber) {
        DroneResponseDTO dto = new DroneResponseDTO();
        dto.setSerialNumber(serialNumber);
        dto.setModel(Model.Heavyweight);
        dto.setBatteryCapacity(100);
        dto.setState(State.IDLE);
        dto.setWeightLimit(1000);
        return dto;
    }
}
