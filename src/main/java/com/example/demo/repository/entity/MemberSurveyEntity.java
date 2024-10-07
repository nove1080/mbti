package com.example.demo.repository.entity;

import javax.persistence.*;
import lombok.*;

@Entity(name = "member_survey_entity")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberSurveyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_survey_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	private double version;

	private String answer;
}
