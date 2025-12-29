/**
 * 작업내용: 매칭방 알림 헬퍼 함수 - 매칭방 페이지에서 쉽게 사용할 수 있는 알림 전송 헬퍼 함수들
 * 
 * @author 김지번
 * @DateOfCreated 2025-12-29
 * @DateOfEdit 2025-12-29
 */

import { requestJoinRoom, joinRoom, approveJoinRoom } from "../api/roomApi.js";
import { getUserId } from "../api/userApi.js";

/**
 * 매칭방 입장 요청 알림 전송 (유저가 입장 요청 시 호출)
 * @param {string} roomId - 방 ID
 * @param {string} hostId - 방장 ID
 * @param {string} userName - 요청하는 유저 이름 (현재 사용자의 닉네임 등)
 */
export const sendJoinRequestNotification = async (roomId, hostId, userName) => {
    try {
        const userId = getUserId();
        if (!userId) {
            throw new Error("로그인이 필요합니다.");
        }

        await requestJoinRoom(roomId, userId, userName, hostId);
        console.log("입장 요청 알림 전송 완료");
    } catch (error) {
        console.error("입장 요청 알림 전송 실패:", error);
        throw error;
    }
};

/**
 * 매칭방 입장 완료 알림 전송 (유저가 즉시 입장 시 호출)
 * @param {string} roomId - 방 ID
 * @param {string} hostId - 방장 ID
 * @param {string} userName - 입장하는 유저 이름
 */
export const sendJoinNotification = async (roomId, hostId, userName) => {
    try {
        const userId = getUserId();
        if (!userId) {
            throw new Error("로그인이 필요합니다.");
        }

        await joinRoom(roomId, userId, userName, hostId);
        console.log("입장 완료 알림 전송 완료");
    } catch (error) {
        console.error("입장 완료 알림 전송 실패:", error);
        throw error;
    }
};

/**
 * 매칭방 입장 허가 알림 전송 (방장이 유저의 입장을 허가 시 호출)
 * @param {string} roomId - 방 ID
 * @param {string} userId - 허가할 유저 ID
 */
export const sendApprovalNotification = async (roomId, userId) => {
    try {
        const hostId = getUserId();
        if (!hostId) {
            throw new Error("로그인이 필요합니다.");
        }

        await approveJoinRoom(roomId, hostId, userId);
        console.log("입장 허가 알림 전송 완료");
    } catch (error) {
        console.error("입장 허가 알림 전송 실패:", error);
        throw error;
    }
};

/**
 * 사용 예제:
 * 
 * // 1. 유저가 방에 입장 요청 시
 * import { sendJoinRequestNotification } from "../utils/roomNotificationHelper";
 * 
 * const handleRequestJoin = async () => {
 *     try {
 *         await sendJoinRequestNotification(roomId, hostId, "사용자닉네임");
 *         // 추가 로직 (예: 요청 상태 업데이트 등)
 *     } catch (error) {
 *         alert(error.message);
 *     }
 * };
 * 
 * // 2. 유저가 즉시 입장 시
 * import { sendJoinNotification } from "../utils/roomNotificationHelper";
 * 
 * const handleJoinRoom = async () => {
 *     try {
 *         await sendJoinNotification(roomId, hostId, "사용자닉네임");
 *         // 추가 로직 (예: 방 참여자 목록 업데이트 등)
 *     } catch (error) {
 *         alert(error.message);
 *     }
 * };
 * 
 * // 3. 방장이 입장 요청 승인 시
 * import { sendApprovalNotification } from "../utils/roomNotificationHelper";
 * 
 * const handleApproveJoin = async (requestingUserId) => {
 *     try {
 *         await sendApprovalNotification(roomId, requestingUserId);
 *         // 추가 로직 (예: 참여자 목록 업데이트 등)
 *     } catch (error) {
 *         alert(error.message);
 *     }
 * };
 */

