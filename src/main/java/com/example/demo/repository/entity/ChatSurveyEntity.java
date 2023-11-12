package com.example.demo.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatSurveyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_survey_id")
	private Long id;

	private double version;

	private String answer;
}
