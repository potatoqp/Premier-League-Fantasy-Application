import { useEffect, useState } from 'react';

function App() {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [filters, setFilters] = useState({
    name: '',
    team: '',
    position: ''
  });

  useEffect(() => {
    const params = new URLSearchParams();

    if (filters.name) params.set('name', filters.name);
    if (filters.team) params.set('team', filters.team);
    if (filters.position) params.set('position', filters.position);

    const query = params.toString();
    const url = `/api/v1/player${query ? `?${query}` : ''}`;

    setLoading(true);
    setError('');

    fetch(url)
      .then((response) => {
        if (!response.ok) {
          throw new Error('Unable to load players from the backend');
        }
        return response.json();
      })
      .then((data) => {
        setPlayers(data);
      })
      .catch((err) => {
        setError(err.message);
        setPlayers([]);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [filters.name, filters.team, filters.position]);

  const handleFilterChange = (event) => {
    const { name, value } = event.target;
    setFilters((current) => ({ ...current, [name]: value }));
  };

  const resetFilters = () => {
    setFilters({ name: '', team: '', position: '' });
  };

  return (
    <div className="app-shell">
      <header className="hero">
        <div>
          <p className="eyebrow">Premier Fantasy</p>
          <h1>Discover your squad</h1>
          <p className="hero-copy">
            Browse Premier League players, filter by team or role, and get a quick snapshot of the current roster.
          </p>
        </div>
      </header>

      <section className="filters">
        <div className="filter-group">
          <label htmlFor="name">Player name</label>
          <input id="name" name="name" value={filters.name} onChange={handleFilterChange} placeholder="Search by name" />
        </div>
        <div className="filter-group">
          <label htmlFor="team">Team</label>
          <input id="team" name="team" value={filters.team} onChange={handleFilterChange} placeholder="e.g. Arsenal" />
        </div>
        <div className="filter-group">
          <label htmlFor="position">Position</label>
          <input id="position" name="position" value={filters.position} onChange={handleFilterChange} placeholder="e.g. FW" />
        </div>
        <button type="button" onClick={resetFilters}>Reset</button>
      </section>

      <section className="results">
        {loading && <p className="status">Loading players...</p>}
        {error && <p className="status error">{error}</p>}
        {!loading && !error && players.length === 0 && <p className="status">No players matched these filters.</p>}

        <div className="card-grid">
          {Array.from(
            new Map(
              players
                .filter((player) => {
                  const name = (player.name || '').trim();
                  return name && !name.toLowerCase().includes('squad total');
                })
                .map((player) => [
                  `${player.name || ''}|${player.team_name || ''}|${player.position || ''}|${player.nation || ''}`,
                  player
                ])
            ).values()
          ).map((player) => (
            <article className="player-card" key={`${player.name || ''}|${player.team_name || ''}|${player.position || ''}|${player.nation || ''}`}>
              <div className="card-header">
                <h2>{player.name || 'Unnamed player'}</h2>
                <span>{player.position || '—'}</span>
              </div>
              <p><strong>Team:</strong> {player.team_name || 'Unknown'}</p>
              <p><strong>Nation:</strong> {player.nation || '—'}</p>
              <p><strong>Age:</strong> {player.age ?? '—'}</p>
              <p><strong>Goals:</strong> {player.goals ?? '—'}</p>
              <p><strong>Assists:</strong> {player.assists ?? '—'}</p>
            </article>
          ))}
        </div>
      </section>
    </div>
  );
}

export default App;
