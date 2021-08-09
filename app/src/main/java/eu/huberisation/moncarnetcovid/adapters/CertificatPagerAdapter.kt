package eu.huberisation.moncarnetcovid.adapters

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import eu.huberisation.moncarnetcovid.entities.Certificat
import eu.huberisation.moncarnetcovid.ui.CertificatFragment
import eu.huberisation.moncarnetcovid.ui.ListeCertificatsFragment

class CertificatPagerAdapter(val fragment: ListeCertificatsFragment): FragmentStateAdapter(fragment) {
    private var items: MutableList<Certificat> = mutableListOf()
    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        val certificat = items[position]
        return CertificatFragment.newInstance(certificat).apply {
            setOnCardClickListener {
                fragment.onCertificateClick(certificat)
            }
        }
    }

    fun setItems(certificats: List<Certificat>) {
        val callback = CertificatComparator(items, certificats)
        val diff = DiffUtil.calculateDiff(callback)
        items.clear()
        items.addAll(certificats)
        diff.dispatchUpdatesTo(this)
    }

    class CertificatComparator(private val oldItems: List<Certificat>,
                               private val newItems: List<Certificat>): DiffUtil.Callback() {
        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.code == newItem.code
        }
    }
}