/*
 * Copyright (C) 2018 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.androidveil

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.androidveil.databinding.ItemVeiledLayoutAndroidveilSkydovesBinding

internal class VeiledAdapter(
  @LayoutRes private val userLayout: Int,
  private val onItemClickListener: VeiledItemOnClickListener? = null
) : RecyclerView.Adapter<VeiledAdapter.VeiledViewHolder>() {

  private val veilParamList: MutableList<VeilParams> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VeiledViewHolder {
    val binding = ItemVeiledLayoutAndroidveilSkydovesBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      false
    )
    return VeiledViewHolder(binding).apply {
      binding.root.setOnClickListener {
        val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
          ?: return@setOnClickListener
        onItemClickListener?.onItemClicked(position)
      }
    }
  }

  override fun onBindViewHolder(holder: VeiledViewHolder, position: Int) {
    val veilParams = getVeilParams(position)
    with(holder.binding.itemVeilLayoutMain) {
      if (layout == -1) {
        layout = userLayout
        veilParams.shimmer?.let {
          shimmer = it
        } ?: let {
          shimmer = colorShimmer {
            setBaseColor(veilParams.baseColor)
            setBaseAlpha(veilParams.baseAlpha)
            setHighlightColor(veilParams.highlightColor)
            setHighlightAlpha(veilParams.highlightAlpha)
            setDropoff(veilParams.dropOff)
          }
        }
        radius = veilParams.radius
        drawable = veilParams.drawable
        shimmerEnable = veilParams.shimmerEnable
        defaultChildVisible = veilParams.defaultChildVisible
      } else {
        startShimmer()
      }
      veil()
    }
  }

  private fun getVeilParams(position: Int): VeilParams = veilParamList[position]

  fun updateParams(params: List<VeilParams>) {
    veilParamList.clear()
    veilParamList.addAll(params)
    notifyDataSetChanged()
  }

  override fun getItemCount() = veilParamList.size

  class VeiledViewHolder(val binding: ItemVeiledLayoutAndroidveilSkydovesBinding) :
    RecyclerView.ViewHolder(binding.root)
}
