

//채팅방을 나누지 않는 상황을 가정

// brokerURL 을 설정 -> WebSocketConfig에 설정된 엔드포인트로 연결
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/coding-test',
    connectHeaders: {
        name: "unknown user"
    }
});

// 연결이 성공하면, /topic/greetings를 구독하게 됨
stompClient.onConnect = (frame) => {

    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe(`/topic/chat-messages`, (greeting) => {
        showMessage(JSON.parse(greeting.body).content);
    });

    stompClient.subscribe('/topic/codes', (codeMessage) => {
        updateCode(JSON.parse(codeMessage.body).code);
    });

    stompClient.publish({
        destination: "/app/enter",
        body: JSON.stringify({'name': $("#name").val()})
    });

    //이 아래는 입장할 방을 나누는 버전
    // const roomNumber = $("#room").val();
    // setConnected(true);
    // console.log('Connected: ' + frame);
    // stompClient.subscribe(`/topic/chat-messages/${roomNumber}`, (greeting) => {
    //     showMessage(JSON.parse(greeting.body).content);
    // });
    // stompClient.subscribe(`/topic/codes/${roomNumber}`, (codeMessage) => {
    //     updateCode(JSON.parse(codeMessage.body).code);
    // });
    // stompClient.publish({
    //     destination: `/app/enter/${roomNumber}`,
    //     body: JSON.stringify({'name': $("#name").val()})
    // });
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
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
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
    stompClient.publish({
        destination: "/app/exit",
    });

    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

// name을 /app/hello 로 전송
function sendMessage() {
    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({'chat': $("#chat").val()})
    });
}

function sendCode() {
    stompClient.publish({
        destination: "/app/update-code",
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