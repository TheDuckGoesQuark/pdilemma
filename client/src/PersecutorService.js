export const httpCodes = {
    conflict: 409,
    notFound: 404
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

    if (response.ok) return body.prisonerId;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function addPrisonerToGame(gameId) {
    const response = await fetch(`/prosecutor/games/${gameId}/prisoners`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
    });
    const body = await response.json();

    if (response.ok) return body.prisonerId;
    else return Promise.reject({status: response.status, message: body.message});
}

export async function makeChoice(choice) {
    let choiceObj = {choice: choice};

    const response = await fetch('/prosecutor/games/1/prisoners/1', {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(choiceObj)
    });
    const body = await response.json();

    if (response.ok) return body;
    else return Promise.reject(`Error code ${response.status} given`)
}