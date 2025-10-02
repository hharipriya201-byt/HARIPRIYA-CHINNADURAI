Smart Office demo.

Features:
- Configure rooms: `config rooms <count>`
- Set max capacity: `set maxcapacity <roomId> <capacity>`
- Book: `block <roomId> <minutes>`
- Cancel: `cancel <roomId>`
- Add occupants (sensor): `addoccupant <roomId> <count>`
- Status: `status <roomId>`
- Type `exit` to quit.

Notes:
- Bookings auto-release if room remains unoccupied for 5 minutes (configurable in BookingManager).
- Uses Observer, Singleton and Command patterns.
- Console-friendly; no infinite hard-coded loops like `while(true)` without condition.
