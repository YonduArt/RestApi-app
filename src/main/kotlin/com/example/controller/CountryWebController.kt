package com.example.controller

import com.example.dto.CountryDto
import com.example.dto.CityDto
import com.example.exception.CountryNotFoundException
import com.example.service.CountryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/")
class CountryWebController(
    private val countryService: CountryService,
) {

    @GetMapping
    fun index(@RequestParam(name = "page", defaultValue = "0") page: Int,
              @RequestParam(name = "search", required = false) search: String?,
              model: Model): String {
        val countries = if (search.isNullOrBlank()) {
            countryService.getAll(page)
        } else {
            countryService.search(search)
        }
        model.addAttribute("countries", countries)
        model.addAttribute("currentPage", page)
        model.addAttribute("search", search ?: "")
        return "countries"
    }

    @GetMapping("/country/{id}")
    fun getCountry(@PathVariable id: Int, model: Model): String {
        return try {
            val country = countryService.getById(id)
            model.addAttribute("country", country)
            "country-detail"
        } catch (e: CountryNotFoundException) {
            model.addAttribute("error", "Страна с ID $id не найдена")
            "error"
        } catch (e: Exception) {
            model.addAttribute("error", "Ошибка при загрузке страны: ${e.javaClass.simpleName} - ${e.message}")
            "error"
        }
    }

    @GetMapping("/country/new")
    fun newCountryForm(model: Model): String {
        model.addAttribute("country", CountryDto(name = "", population = 0, cities = emptyList()))
        model.addAttribute("isEdit", false)
        return "country-form"
    }

    @GetMapping("/country/{id}/edit")
    fun editCountryForm(@PathVariable id: Int, model: Model): String {
        try {
            val country = countryService.getById(id)
            model.addAttribute("country", country)
            model.addAttribute("isEdit", true)
            return "country-form"
        } catch (e: CountryNotFoundException) {
            model.addAttribute("error", "Страна с ID $id не найдена")
            return "error"
        }
    }

    @PostMapping("/country")
    fun createCountry(
        @RequestParam name: String?,
        @RequestParam population: Int?,
        @RequestParam(required = false) cityNames: List<String>?,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            if (name.isNullOrBlank()) {
                redirectAttributes.addFlashAttribute("error", "Название страны не может быть пустым")
                return "redirect:/country/new"
            }
            
            val populationValue = population ?: 0
            val cities = (cityNames ?: emptyList())
                .filter { it.isNotBlank() }
                .map { CityDto(it.trim()) }
            val countryDto = CountryDto(name = name.trim(), population = populationValue, cities = cities)
            
            val id = countryService.create(countryDto)
            redirectAttributes.addFlashAttribute("message", "Страна успешно создана!")
            "redirect:/country/$id"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании страны: ${e.message}")
            "redirect:/country/new"
        }
    }

    @PostMapping("/country/{id}")
    fun updateCountry(
        @PathVariable id: Int,
        @RequestParam name: String?,
        @RequestParam population: Int?,
        @RequestParam(required = false) cityNames: List<String>?,
        redirectAttributes: RedirectAttributes
    ): String {
        return try {
            if (name.isNullOrBlank()) {
                redirectAttributes.addFlashAttribute("error", "Название страны не может быть пустым")
                return "redirect:/country/$id/edit"
            }
            
            val populationValue = population ?: 0
            val cities = (cityNames ?: emptyList())
                .filter { it.isNotBlank() }
                .map { CityDto(it.trim()) }
            val countryDto = CountryDto(name = name.trim(), population = populationValue, cities = cities)
            
            countryService.update(id, countryDto)
            redirectAttributes.addFlashAttribute("message", "Страна успешно обновлена!")
            "redirect:/country/$id"
        } catch (e: CountryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", "Страна с ID $id не найдена")
            "redirect:/"
        } catch (e: Exception) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении страны: ${e.message}")
            "redirect:/country/$id/edit"
        }
    }

    @PostMapping("/country/{id}/delete")
    fun deleteCountry(@PathVariable id: Int, redirectAttributes: RedirectAttributes): String {
        return try {
            countryService.delete(id)
            redirectAttributes.addFlashAttribute("message", "Страна успешно удалена!")
            "redirect:/"
        } catch (e: CountryNotFoundException) {
            redirectAttributes.addFlashAttribute("error", "Страна с ID $id не найдена")
            "redirect:/"
        }
    }
}

