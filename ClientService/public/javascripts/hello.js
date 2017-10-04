window.onLoad = function()
    {
        var video = document.getElementById("live_video")

        if(video === null)
        {
            console.log("Video was null")
            return;
        }

        if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia)
        {
            navigator.mediaDevices.getUserMedia({ video: true })
                .then(function(stream)
                {
                    video.src = window.URL.createObjectURL(stream)
                    video.play()
                });
        }
    }
