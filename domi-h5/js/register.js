var options = {
    useEasing: true,
    useGrouping: true,
    separator: ',',
    decimal: '.',
};

setTimeout(function () {
    var demo = new CountUp('may-borrow-money', 0, 200000, 0, 2.5, options);
    if (!demo.error) {
        demo.start();
    } else {
        console.error(demo.error);
    }
},500);

window.onresize = function () {
    if (document.activeElement.tagName == "INPUT" || document.activeElement.tagName == "TEXTAREA") {
        setTimeout(function () {
            var top = document.activeElement.getBoundingClientRect().top;
            window.scrollTo(0,top);
        }, 0);
    }
    $('input').scrollIntoView();
};



