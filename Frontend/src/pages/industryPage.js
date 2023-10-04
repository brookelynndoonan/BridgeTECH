document.addEventListener("DOMContentLoaded", function() {
    // Fetch the list of industries in need of software engineers from the backend
    fetch('/industry/list', {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            // Populate the #industries-container with the fetched data
            const industriesContainer = document.getElementById("industries-container");
            industriesContainer.innerHTML = data.map(industry => `<div class="industry-item">${industry.name}</div>`).join('');
        });

    // Fetch user-specific industry recommendations from the backend
    fetch('/recommendations/generate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ userId: 'current_user_id' })  // Replace 'current_user_id' with actual user ID or token
    })
        .then(response => response.json())
        .then(data => {
            // Populate the #recommendations-container with the fetched data
            const recommendationsContainer = document.getElementById("recommendations-container");
            recommendationsContainer.innerHTML = data.map(recommendation => `<div class="recommendation-item">${recommendation.name}</div>`).join('');
        });
});
