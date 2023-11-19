package com.example.demo.repository.entity;

import com.example.demo.repository.entity.constant.ChatRoomStatus;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity(name = "chat_room_list_entity")
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatRoomListEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_list_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id")
	private ChatRoomEntity chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private MemberEntity member;

	@Enumerated(EnumType.STRING)
	@Builder.Default
	@ColumnDefault("'SURVEY'")
	private ChatRoomStatus chatStatus = ChatRoomStatus.SURVEY;

	public void changeChatStatus(ChatRoomStatus chatStatus) {
		this.chatStatus = chatStatus;
	}
}
