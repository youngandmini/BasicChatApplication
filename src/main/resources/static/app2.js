

// 채팅방을 나누도록 설정 - ChatRoomController와 연결


// brokerURL 을 설정 -> WebSocketConfig에 설정된 엔드포인트로 연결
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/coding-test',
    connectHeaders: {
        name: "unknown user"
    }
});

// 연결이 성공하면,
// /topic/chat-messages와
// /topic/codes/를
// 구독하게 됨
stompClient.onConnect = (frame) => {

    //입장할 방을 나누는 버전
    const roomNumber = $("#room").val();
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe(`/topic/chat-messages/${roomNumber}`, (chatMessage) => {
        showMessage(JSON.parse(chatMessage.body).content);
    });
    stompClient.subscribe(`/topic/codes/${roomNumber}`, (codeMessage) => {
        updateCode(JSON.parse(codeMessage.body).code);
    });
    stompClient.publish({
        destination: `/app/enter/${roomNumber}`,
        body: JSON.stringify({'name': $("#name").val()})
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);

    $("#chatMessages").html("");
    updateCode("");
}

function setNameHeader() {
    stompClient.connectHeaders = {
        name: $("#name").val()
    };
}

// 설정된 brokerURL로 연결을 활성화
function connect() {

    setNameHeader();
    stompClient.activate();
}

// 활성화된 연결을 해제
function disconnect() {
    const roomNumber = $("#room").val();

    stompClient.publish({
        destination: `/app/exit/${roomNumber}`,
    });

    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// name을 /app/hello 로 전송
function sendMessage() {
    const roomNumber = $("#room").val();

    stompClient.publish({
        destination: `/app/chat/${roomNumber}`,
        body: JSON.stringify({'chat': $("#chat").val()})
    });
}

function sendCode() {
    const roomNumber = $("#room").val();

    stompClient.publish({
        destination: `/app/update-code/${roomNumber}`,
        body: JSON.stringify({'code': $("#code").val()})
    });
}

function showMessage(message) {
    $("#chatMessages").append("<tr><td>" + message + "</td></tr>");
}

function updateCode(code) {
    $("#code").val(code);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendMessage());

    $( "#sendCode" ).click(() => sendCode());
});