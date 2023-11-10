package com.example.demo.repository.entity;

import javax.persistence.*;

import com.example.demo.repository.entity.constant.ChatRoomStatus;
import lombok.*;

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

	private String title;
	private String password;

	private ChatRoomStatus status;

	@OneToMany
	@JoinColumn(name = "chat_survey_id")
	private List<ChatSurveyEntity> chatSurvey;
}
