<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkań.
           </span>
    <h3 v-else>
      Zaplanowane zajęcia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
        mounted() {
            this.$http.get('meetings').then(response => {
                this.meetings = response.body;
            });
        },
        methods: {
            addNewMeeting(meeting) {
                this.$http.post('meetings', meeting).then(response => {
                    const addedMeeting = response.body;
                    this.meetings.push(addedMeeting);
                });
            },
            addMeetingParticipant(meeting) {
                this.$http.post(`meetings/${meeting.id}/participants`)
                    .then(response => meeting.participants.push(response.body));
            },
            removeMeetingParticipant(meeting) {
                this.$http.delete(`meetings/${meeting.id}/participants/me`)
                    .then(() => meeting.participants.splice(meeting.participants.map(p => p.login).indexOf(this.username), 1));

            },
            deleteMeeting(meeting) {
                this.$http.delete(`meetings/${meeting.id}`).then(() => this.meetings.splice(this.meetings.indexOf(meeting), 1));
            }
        }
    }
</script>
