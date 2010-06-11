package comms;

import java.util.*;

/**
 * Represents a list of peers of the current client.
 */
public class PeerList {

	// Members
	private ArrayList<PeerData> mPeerList;

	/**
	 * Constructor.
	 */
	public PeerList() {
		mPeerList = new ArrayList<PeerData>();
	}

	/**
	 * Add peer data to the internal list.
	 * @param peerData The client data to add.
	 */
	public void add(PeerData peerData) {
		mPeerList.add(peerData);
	}

	/**
	 * Get the peer data at the specified index.
	 * @param index The index of the data to retrieve.
	 * @return The peer data.
	 */
	public PeerData get(int index) {
		return mPeerList.get(index);
	}

	/**
	 * Get the number of peer data in this session.
	 * @return The number of peer data in this session.
	 */
	public int getPeerDataCount() { return mPeerList.size(); }

	/**
	 * Get a string representation of the peer data.
	 * @return A string representation of the peer data.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (PeerData data : mPeerList) {
			builder.append(data.toString());
		}
		
		return builder.toString();
	}
}
