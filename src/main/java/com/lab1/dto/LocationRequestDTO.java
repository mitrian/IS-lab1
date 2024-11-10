package com.lab1.dto;

public record LocationRequestDTO(long x, Long y, Float z, String name) {

//    public Location toEntity(){
//        Location location = new Location();
//        location.setX(this.x);
//        location.setY(this.y);
//        location.setZ(this.z);
//        location.setName(this.name);
//        return location;
//    }
}
