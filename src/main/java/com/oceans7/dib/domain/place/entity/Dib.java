package com.oceans7.dib.domain.place.entity;

import com.oceans7.dib.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Dib {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dib_id")
    private Long dibId;

    @Column(name = "content_id")
    private Long contentId;

    @Column(name = "content_type_id")
    private int contentTypeId;

    @Column(name = "title")
    private String title;

    @Column(name = "address")
    private String address;

    @Column(name = "tel")
    private String tel;

    @Column(name = "first_image")
    private String firstImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    public static Dib of(Long contentId, int contentTypeId, String title,
                         String address, String tel, String firstImage, User user) {
        Dib dib = new Dib();
        dib.contentId = contentId;
        dib.contentTypeId = contentTypeId;
        dib.title = title;
        dib.address = address;
        dib.tel = tel;
        dib.firstImage = firstImage;
        dib.user = user;

        return dib;
    }
}
