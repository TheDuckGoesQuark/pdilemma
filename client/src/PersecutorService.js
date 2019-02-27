export const httpCodes = {
    conflict: 409,
    notFound: 404,
    preconditionFailed: 412
};

export async function testConnection() {
    try {
        const response = await fetch('/prosecutor/test');
        const body = await response.text();

        if (response.ok) return body;
    } catch (ignored) {
    }

    return Promise.reject("Failed to Connect.")
}

export async function getJoinableGames() {
    const response = await fetch('/prosecutor/games?joinable=true', {
        method: "GET",
        headers: {"Content-Type": "application/json"},
    });
    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function getPrisonerFromGame(gameId, prisonerId) {
    const response = await fetch(`/prosecutor/games/${gameId}/prisoners/${prisonerId}`, {
        method: "GET",
        headers: {"Content-Type": "application/json"},
    });
    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function addPrisonerToGame(gameId) {
    const response = await fetch(`/prosecutor/games/${gameId}/prisoners`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
    });
    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function startGame() {
    const response = await fetch(`/prosecutor/games/`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
    });

    const body = await response.json();

    if (response.ok) return body.gameId;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function makeChoice(gameId, prisonerId, choice) {
    let choiceObj = {choice: choice};

    const response = await fetch(`/prosecutor/games/${gameId}/prisoners/${prisonerId}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(choiceObj)
    });
    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function getYearsReduction(gameId, prisonerId) {
    const response = await fetch(`/prosecutor/games/${gameId}/prisoners/${prisonerId}/numYearsReduction`, {
        headers: {"Content-Type": "application/json"},
    });

    const body = await response.json();

    if (response.ok) return body.numYearsReduction;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function getAllGames() {
    const response = await fetch('/prosecutor/games', {
        headers: {"Content-Type": "application/json"},
    });

    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject({status: response.status, message: body.message});
}
