var messages = [];

$(document).ready(function() {
    getMessages();
});

$('#send-btn').click(function(){
    sendMessage();
});

$("#msg-input").keypress(function(e) {
    if (e.which == 13) {
        sendMessage();
    }
});


var monthNames = [
    "January", "February", "March",
    "April", "May", "June", "July",
    "August", "September", "October",
    "November", "December"
];

function getTimeString(timestamp) {
    var date = new Date(timestamp);

    var day = date.getDay();
    var month = monthNames[date.getMonth()].substr(0, 3);
    var hours = date.getHours();
    var minutes = "0" + date.getMinutes();

    var formattedTime = day + " " + month + " " + hours + ':' + minutes.substr(-2);
    return formattedTime;
}


function refreshMessages() {
    var messageList = $("#message-list");
    messageList.empty();

    for (var i=0; i<messages.length; ++i) {
        var message = messages[i];
        var element = $("#message-template").clone();
        element.find("author").text(users[message.posted_by]);
        element.find("time").text(getTimeString(message.posted_at));
        element.find("p").text(message.message);
        element.removeAttr("hidden");
        element.removeAttr("id");
        element.appendTo(messageList);
    }
}

function addMessage(data) {
    for (var i=0; i<messages.length; ++i)
        if (messages[i].pk == data.pk) {
            messages[i] = data
            return;
        }

   messages.push(data);
}

function getMessages() {
    // Get latest 20 messages.
    var timestamp = new Date().getTime();
    var data = {
        "end_time": timestamp,
        "count": 20,
    };

    $.ajax({
        type: "POST",
        url: "/api/v1/message/get/" + conversationId + "/",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data) {
            for (var i=data.messages.length-1; i>=0; --i)
                addMessage(data.messages[i]);
            refreshMessages();
        },
        error: function(errMsg) {
            console.log(errMsg);
        }
    });
}


function sendMessage() {
    var data = {
        "message": $("#msg-input").val()
    };

    $("#msg-input").val('');
    $.ajax({
        type: "POST",
        url: "/api/v1/message/add/" + conversationId + "/",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data) {
            console.log("success");
            getMessages();
        },
        error: function(errMsg) {
            console.log(errMsg);
        }
    });
}
