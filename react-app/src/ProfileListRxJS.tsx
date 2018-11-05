import React, { Component } from 'react';
import { startWith, switchMap } from 'rxjs/operators';
import { interval } from 'rxjs';

interface Profile {
  id: number;
  email: string;
}

interface ProfileListProps {
}

interface ProfileListState {
  profiles: Array<Profile>;
  isLoading: boolean;
}

class ProfileList extends Component<ProfileListProps, ProfileListState> {

  constructor(props: ProfileListProps) {
    super(props);

    this.state = {
      profiles: [],
      isLoading: false
    };
  }

  async componentDidMount() {
    this.setState({isLoading: true});

    const request = interval(1000).pipe( // <1>
      startWith(0), // <2>
      switchMap(() => // <3>
        fetch('http://localhost:3000/profiles')
          .then((response) => response.json())
      ));

    request.subscribe((data: any) => { // <4>
      this.setState({profiles: data, isLoading: false});
    })
  }

  render() {
    const {profiles, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div>
        <h2>Profile List</h2>
        {profiles.map((profile: Profile) =>
          <div key={profile.id}>
            {profile.email}<br/>
          </div>
        )}
        <a href="/" className="App-link">Home</a>
      </div>
    );
  }
}

export default ProfileList;