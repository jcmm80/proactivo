const navMenu = document.getElementById('nav_menu'),
    navToggle = document.getElementById('nav_toggle'),
    navClose = document.getElementById('nav_close');

if (navToggle) {
    navToggle.addEventListener('click', () => {
        navMenu.classList.add('show_menu');

    });

}

if(navClose){
    navClose.addEventListener('click', () => {
        navMenu.classList.remove('show_menu');

    });
}

const navLinkV = document.querySelectorAll('.nav-vertical-link');
const navLinkH = document.querySelectorAll('.nav-horizontal-link');
const linkAction = () =>{
    navMenu.classList.remove('show-menu');

};
navLinkV.forEach(n => n.addEventListener('click', linkAction));
navLinkH.forEach(n => n.addEventListener('click', linkAction));
