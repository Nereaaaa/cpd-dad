package es.codeurjc.api.Model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Instance {

    @Id
    private UUID id;

    private String name;
    
    private int memory;
    private int cores;
    
    private String ip;

    @Enumerated(EnumType.STRING)
    private InstanceStatus status;

    @OneToOne
    @JoinColumn(name = "disk_id", unique = true) 
    private Disk disk; //PONER QUE ES DISKTYPE Y DISKSIZE ??
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getCores() {
        return cores;
    }

    public void setCores(int cores) {
        this.cores = cores;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public InstanceStatus getStatus() {
        return status;
    }

    public void setStatus(InstanceStatus status) {
        this.status = status;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setDisk(Disk disk) {
        this.disk = disk;
    }
}