package com.example.demo.repository.entity;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(unique = true)
	private String email;

	private String name;

	private String password;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_survey_id")
	private MemberSurveyEntity memberSurvey;

	public boolean isPassword(String target) {
		return password.equals(target);
	}

	@Builder.Default
	@Column(nullable = false)
	private Boolean deleted = false;

	public void delete() {
		this.deleted = true;
	}

}
