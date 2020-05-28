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
        methods: {
            addNewMeeting: function (meeting) {
                this.meetings.push(meeting);
                this.$http.post('meetings', meeting)
                    .then(() => {
                      //  this.success('Konto zostało założone. Możesz się zalogować.');
                        //  this.registering = false;
                    })
                   // .catch(response => this.failure('Błąd przy zakładaniu konta. Kod odpowiedzi: ' + response.status));
            },

            addMeetingParticipant(meeting) {
                meeting.participants.push(this.username);
            },
            removeMeetingParticipant(meeting) {
                meeting.participants.splice(meeting.participants.indexOf(this.username), 1);
            },
            deleteMeeting(meeting) {
                this.meetings.splice(this.meetings.indexOf(meeting), 1);
            }
        },
        mounted() {
            const meeting = localStorage.getItem('meeting');

            if (meting) {
                this.storeAuth(meeting);

               // this.$http.get(`participants/${username}`).catch(() => this.logout());
            }
        }
    }
</script>
