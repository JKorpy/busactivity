import { useState } from 'react'

function App() {
    const [routeId, setRouteId] = useState("");
    const [results, setResults] = useState(null); // array of buses | null
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const searchRoute = async (event) => {
        event.preventDefault();

        const id = routeId.trim().toLocaleUpperCase();
        if (!id) {
            setError("Please enter a route ID");
            setResults(null);
            return;
        }

        setLoading(true);
        setError("");
        setResults(null);

        try {
            const response = await fetch(`api/routes/${encodeURIComponent(id)}`);
            if (!response.ok) {
                throw new Error(response.status === 404 ? "Route not found" : `HTTP ${response.status}`);
            }
            const data = await response.json();
            console.log("Route:", data);
            setResults(data);
        } catch (err) {
            console.error("Failed to fetch route:", err);
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <h1 class="text-3xl font-bold underline">React Route</h1>

            <form onSubmit={searchRoute}>
                <input
                    type="text"
                    placeholder="Enter route ID"
                    value={routeId}
                    onChange={(event) => setRouteId(event.target.value)}
                />
                <button type="submit" disabled={loading}>
                    {loading ? "Searching..." : "Search"}
                </button>
            </form>

            {results && (
                <ul class="list-none">
                    {results.map((bus) => (
                        <li key={bus.vehicleId}>
                            Vehicle {bus.vehicleId}: lat {bus.latitude}, lon {bus.longitude}
                        </li>
                    ))}
                </ul>
            )}

            {error && (
                <ul>
                    <li>{error}</li>
                </ul>
            )}
        </>
    );
}

export default App