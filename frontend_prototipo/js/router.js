import {renderLogin} from "./views/login/loginView.js";

import {renderCadastro} from "./views/cadastro/cadastroView.js";
import {setupCadastroEvents} from "./views/cadastro/cadastroEvents.js";

const routers = {
   "/": {
    render: renderLogin,
   },
   "/login": {
    render: renderLogin,
   },
   "/cadastro": {
    render: renderCadastro,
    setupEvents: setupCadastroEvents
   }
};

export function navigateTo(path) {
    history.pushState({}, "", path);
    router();
}

export function router(){

    const path = window.location.pathname;
    const render = routers[path] || routers["/"];

    const app = document.getElementById("app");

    render.render(app);    

    if (render.setupEvents) {
        render.setupEvents(app);
    }
}
