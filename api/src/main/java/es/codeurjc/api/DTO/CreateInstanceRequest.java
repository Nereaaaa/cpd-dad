package es.codeurjc.api.DTO;

import es.codeurjc.api.Model.DiskType;

public class CreateInstanceRequest {
	//Instance
    private String name;
    private int memory;
    private int cores;

    //Disk
    private int diskSize;
    private DiskType diskType;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getMemory() { return memory; }
    public void setMemory(int memory) { this.memory = memory; }

    public int getCores() { return cores; }
    public void setCores(int cores) { this.cores = cores; }

    public int getDiskSize() { return diskSize; }
    public void setDiskSize(int diskSize) { this.diskSize = diskSize; }

    public DiskType getDiskType() { return diskType; }
    public void setDiskType(DiskType diskType) { this.diskType = diskType; }
}