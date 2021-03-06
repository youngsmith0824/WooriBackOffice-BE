package com.woori.wooribackoffice.domain.entity;

import com.woori.wooribackoffice.domain.dto.request.FarmRequest;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "farm")
public class Farm extends AuditBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String owner;

    private String address;

    public void update(FarmRequest farmRequest) {
        this.name = farmRequest.getName();
        this.owner = farmRequest.getOwner();
        this.address = farmRequest.getAddress();
    }

    @Override
    public String toString() {
        return "Farm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public static Farm from(FarmRequest farmRequest) {
        return new Farm().setAddress(farmRequest.getAddress())
                .setName(farmRequest.getName())
                .setOwner(farmRequest.getOwner());
    }
}
