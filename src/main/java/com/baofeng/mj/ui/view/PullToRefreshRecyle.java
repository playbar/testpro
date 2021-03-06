/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *******************************************************************************/
package com.baofeng.mj.ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

public class PullToRefreshRecyle extends PullToRefreshBase<RecyclerView> {

	public PullToRefreshRecyle(Context context) {
		super(context);
	}

	public PullToRefreshRecyle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected RecyclerView createRefreshableView(Context context,
			AttributeSet attrs) {
		RecyclerView viewPager = new InternalScrollViewSDK9(context, attrs);
        // viewPager.setId(R.id.recyclerView);
		return viewPager;
	}

	@Override
	protected boolean isReadyForPullStart() {
		RecyclerView refreshableView = getRefreshableView();

		Adapter adapter = refreshableView.getAdapter();
		if (null == adapter) {
			// return refreshableView.getCurrentItem() == 0;
			return true;
		}

		return isTop(refreshableView);
	}

	@Override
	protected boolean isReadyForPullEnd() {
		RecyclerView refreshableView = getRefreshableView();

		Adapter adapter = refreshableView.getAdapter();
		if (null == adapter) {
			// return refreshableView.getCurrentItem() == adapter.getCount() -
			// 1;
			return true;
		}

		return isBottom(refreshableView);
	}

	private boolean isBottom(RecyclerView recyclerView) {
		LinearLayoutManager manager = (LinearLayoutManager) recyclerView
				.getLayoutManager();
		if (manager == null) {
			return true;
		} else {
			return manager.findLastCompletelyVisibleItemPosition() == recyclerView
					.getAdapter().getItemCount() - 1;
		}
	}

	private boolean isTop(RecyclerView recyclerView) {
		LinearLayoutManager manager = (LinearLayoutManager) recyclerView
				.getLayoutManager();

		if (manager == null) {
			return true;
		} else {
			return recyclerView.getAdapter().getItemCount() == 0
					|| manager.findFirstCompletelyVisibleItemPosition() == 0;
		}
	}

	final class InternalScrollViewSDK9 extends RecyclerView {

		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
				int scrollY, int scrollRangeX, int scrollRangeY,
				int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY,
					scrollX, scrollY, scrollRangeX, scrollRangeY,
					maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshRecyle.this, deltaX,
					scrollX, deltaY, scrollY, getScrollRange(), isTouchEvent);

			return returnValue;
		}

		/**
		 * Taken from the AOSP ScrollView source
		 */
		private int getScrollRange() {
			int scrollRange = 0;
			if (getChildCount() > 0) {
				View child = getChildAt(0);
				scrollRange = Math.max(0, child.getHeight()
						- (getHeight() - getPaddingBottom() - getPaddingTop()));
			}
			return scrollRange;
		}
	}
}
