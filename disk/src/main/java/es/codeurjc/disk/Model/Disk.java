package es.codeurjc.disk.Model;
 
 import com.fasterxml.jackson.annotation.JsonInclude;
 import java.util.UUID;
 
 @JsonInclude(JsonInclude.Include.NON_NULL)
 public class Disk {
 
     private Long id;
     private int size;
     private DiskType type;
     private DiskStatus status;
 
     // Getters y setters
     public Long getId() { return id; }
     public void setId(Long id) { this.id = id; }
 
     public int getSize() { return size; }
     public void setSize(int size) { this.size = size; }
 
     public DiskType getType() { return type; }
     public void setType(DiskType type) { this.type = type; }
 
     public DiskStatus getStatus() { return status; }
     public void setStatus(DiskStatus status) { this.status = status; }
 }