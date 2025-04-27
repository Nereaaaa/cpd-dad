package es.codeurjc.api.Model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Disk {

    @Id
    private UUID id;

    private int size;

    @Enumerated(EnumType.STRING)
    private DiskType type;

    @Enumerated(EnumType.STRING)
    private DiskStatus status;

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DiskType getType() {
        return type;
    }

    public void setType(DiskType type) {
        this.type = type;
    }

    public DiskStatus getStatus() {
        return status;
    }

    public void setStatus(DiskStatus status) {
        this.status = status;
    }
}