package com.example.demo.repository.entity;

import com.example.demo.repository.entity.constant.ChatRoomStatus;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @OneToMany
    @JoinColumn(name = "chat_survey_id")
    private List<ChatSurveyEntity> chatSurvey;

    private String title;

    private String password;

    private ChatRoomStatus status;

    private String spot;

    private Integer headcount;

	private Date start;

	private Date end;
}
